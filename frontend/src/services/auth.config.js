const DEFAULT_API_URL =
  window.location.hostname === "localhost"
    ? "http://localhost:8080/mywallet"
    : "https://expense-tracker-backend-9nu7.onrender.com/mywallet";

const API_BASE_URL = process.env.REACT_APP_API_URL || DEFAULT_API_URL;

export default API_BASE_URL.replace(/\/$/, "");
