import { useEffect, useMemo, useState } from "react";
import API_BASE_URL from "../services/auth.config";

const RETRY_INTERVAL_MS = 5000;
const HEALTH_TIMEOUT_MS = 4000;

function BackendStarting({ children }) {
  const [backendReady, setBackendReady] = useState(false);
  const [retryCount, setRetryCount] = useState(0);

  const healthUrl = useMemo(() => {
    return API_BASE_URL.replace(/\/mywallet$/, "") + "/health";
  }, []);

  useEffect(() => {
    let isMounted = true;
    let retryTimer;

    const checkBackend = async () => {
      const controller = new AbortController();
      const timeout = setTimeout(() => controller.abort(), HEALTH_TIMEOUT_MS);

      try {
        const response = await fetch(healthUrl, {
          method: "GET",
          cache: "no-store",
          signal: controller.signal,
        });

        if (isMounted && response.ok) {
          setBackendReady(true);
          return;
        }
      } catch (error) {
        if (isMounted) {
          setRetryCount((count) => count + 1);
        }
      } finally {
        clearTimeout(timeout);
      }

      if (isMounted) {
        retryTimer = setTimeout(checkBackend, RETRY_INTERVAL_MS);
      }
    };

    checkBackend();

    return () => {
      isMounted = false;
      clearTimeout(retryTimer);
    };
  }, [healthUrl]);

  if (backendReady) {
    return children;
  }

  return (
    <main style={styles.screen}>
      <section style={styles.panel} aria-live="polite">
        <div style={styles.spinnerWrap}>
          <div style={styles.spinner} />
        </div>
        <h1 style={styles.title}>Backend is starting...</h1>
        <p style={styles.message}>
          Render server is waking up. Please wait 30-60 seconds.
        </p>
        <div style={styles.retryTrack}>
          <div key={retryCount} style={styles.retryBar} />
        </div>
        <p style={styles.retryText}>Retrying every 5 seconds</p>
      </section>
      <style>
        {`
          @keyframes backend-spin {
            to { transform: rotate(360deg); }
          }

          @keyframes backend-retry {
            from { transform: translateX(-100%); }
            to { transform: translateX(100%); }
          }
        `}
      </style>
    </main>
  );
}

const styles = {
  screen: {
    minHeight: "100vh",
    display: "grid",
    placeItems: "center",
    padding: "24px",
    background: "#f7f8fb",
    color: "#172033",
  },
  panel: {
    width: "min(460px, 100%)",
    textAlign: "center",
    padding: "32px 24px",
  },
  spinnerWrap: {
    width: "76px",
    height: "76px",
    margin: "0 auto 24px",
    display: "grid",
    placeItems: "center",
  },
  spinner: {
    width: "64px",
    height: "64px",
    border: "6px solid #dbe4ef",
    borderTopColor: "#2563eb",
    borderRadius: "50%",
    animation: "backend-spin 0.9s linear infinite",
  },
  title: {
    margin: "0 0 10px",
    fontSize: "clamp(1.6rem, 4vw, 2.1rem)",
    fontWeight: 700,
    letterSpacing: 0,
  },
  message: {
    margin: "0 auto 24px",
    maxWidth: "360px",
    color: "#526174",
    fontSize: "1rem",
    lineHeight: 1.55,
  },
  retryTrack: {
    width: "min(280px, 100%)",
    height: "6px",
    margin: "0 auto 12px",
    overflow: "hidden",
    borderRadius: "999px",
    background: "#dbe4ef",
  },
  retryBar: {
    width: "50%",
    height: "100%",
    borderRadius: "999px",
    background: "#2563eb",
    animation: "backend-retry 5s ease-in-out infinite",
  },
  retryText: {
    margin: 0,
    color: "#6b7788",
    fontSize: "0.92rem",
  },
};

export default BackendStarting;
