import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split(".")[1]));
  } catch (e) {
    return null;
  }
};

const AuthVerify = ({ logOut }) => {
  const location = useLocation();

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user?.token) {
      // ✅ check if user and token exist
      const decodedJwt = parseJwt(user.token);

      // ✅ check if decodedJwt exists and has exp
      if (decodedJwt?.exp && decodedJwt.exp * 1000 < Date.now()) {
        logOut(); // call logOut function passed from props
      }
    }
    // else: user not logged in, do nothing
  }, [location, logOut]);

  return null; // empty component
};

export default AuthVerify;
