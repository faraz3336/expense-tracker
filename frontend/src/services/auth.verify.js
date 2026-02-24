import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split(".")[1]));
  } catch (e) {
    return null;
  }
};

<<<<<<< HEAD
const AuthVerify = ({ logOut }) => {
  const location = useLocation();
=======
const AuthVerify = (props) => {
  let location = useLocation();
>>>>>>> 479978a42a3900b9d3a156c1c2bd13b23d0c3b1c

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"));

<<<<<<< HEAD
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
=======
    if (user) {
      const decodedJwt = parseJwt(user.token);

      if (decodedJwt.exp * 1000 < Date.now()) {
        props.logOut();
      }
    }
  }, [location, props]);

  return <></>;
};

export default AuthVerify;
>>>>>>> 479978a42a3900b9d3a156c1c2bd13b23d0c3b1c
