import axios from "axios";
import API_BASE_URL from "./auth.config";

const register_req = async (username, email, password) => {
<<<<<<< HEAD
  return await axios.post(API_BASE_URL + "/auth/signup", {
    userName: username,
    email: email,
    password: password,
  });
};

const login_req = async (email, password) => {
  const response = await axios.post(API_BASE_URL + "/auth/signin", {
    email,
    password,
  });

  if (response.data.token) {
    console.log(response.data);
    localStorage.setItem("user", JSON.stringify(response.data));
    window.location.reload();
  }

  return response;
};

const verifyRegistrationVerificationCode = async (verificationCode) => {
  return await axios.get(API_BASE_URL + "/auth/signup/verify", {
    params: {
      code: verificationCode,
    },
  });
};

const resendRegistrationVerificationCode = async (email) => {
  return await axios.get(API_BASE_URL + "/auth/signup/resend", {
    params: {
      email: email,
    },
  });
};
=======
  return await axios.post(API_BASE_URL + '/auth/signup', {
      userName: username, 
      email: email, 
      password: password
  })
}

const login_req = async (email, password) => {
  const response = await axios.post(API_BASE_URL + '/auth/signin', {email, password})

  if (response.data.token) {
      console.log(response.data)
      localStorage.setItem("user", JSON.stringify(response.data));
      window.location.reload()
  }

  return response;
}

const verifyRegistrationVerificationCode = async (verificationCode) => {
  return await axios.get(API_BASE_URL + '/auth/signup/verify', {
      params: {
          code: verificationCode
      }
  })
}

const resendRegistrationVerificationCode = async(email) => {
  return await axios.get(API_BASE_URL + "/auth/signup/resend", {
      params: {
          email: email
      }
  })
}
>>>>>>> 479978a42a3900b9d3a156c1c2bd13b23d0c3b1c

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

const logout_req = () => {
  localStorage.removeItem("user");
<<<<<<< HEAD
};

const forgotPasswordVerifyEmail = async (email) => {
  return await axios.get(API_BASE_URL + "/auth/forgotPassword/verifyEmail", {
    params: {
      email: email,
    },
  });
};

const forgotPasswordverifyCode = async (code) => {
  return await axios.get(API_BASE_URL + "/auth/forgotPassword/verifyCode", {
    params: {
      code: code,
    },
  });
};

const resendResetPasswordVerificationCode = async (email) => {
  return await axios.get(API_BASE_URL + "/auth/forgotPassword/resendEmail", {
    params: {
      email: email,
    },
  });
};

const resetPassword = async (email, password) => {
  return await axios.post(API_BASE_URL + "/auth/forgotPassword/resetPassword", {
    email: email,
    currentPassword: "",
    newPassword: password,
  });
};
=======
}

const forgotPasswordVerifyEmail = async (email) => {
  return await axios.get(API_BASE_URL + "/auth/forgotPassword/verifyEmail", {
      params: {
          email: email
      }
  })
}

const forgotPasswordverifyCode = async (code) => {
  return await axios.get(API_BASE_URL + "/auth/forgotPassword/verifyCode", {
      params: {
          code: code
      }
  })
}

const resendResetPasswordVerificationCode = async(email) => {
  return await axios.get(API_BASE_URL + "/auth/forgotPassword/resendEmail", {
      params: {
          email: email
      }
  })
}

const resetPassword = async (email, password) => {
  return await axios.post(API_BASE_URL + '/auth/forgotPassword/resetPassword', {
      email: email, 
      currentPassword: "",
      newPassword: password
  })
}
>>>>>>> 479978a42a3900b9d3a156c1c2bd13b23d0c3b1c

const authHeader = () => {
  const user = getCurrentUser();
  if (user && user.token) {
<<<<<<< HEAD
    return { Authorization: "Bearer " + user.token };
  } else {
    return {};
  }
};
=======
    return { Authorization: 'Bearer ' + user.token };
  } else {
    return {};
  }
}
>>>>>>> 479978a42a3900b9d3a156c1c2bd13b23d0c3b1c

const AuthService = {
  register_req,
  login_req,
  verifyRegistrationVerificationCode,
  resendRegistrationVerificationCode,
  getCurrentUser,
  logout_req,
  forgotPasswordVerifyEmail,
  forgotPasswordverifyCode,
  resendResetPasswordVerificationCode,
  resetPassword,
<<<<<<< HEAD
  authHeader,
};

export default AuthService;
=======
  authHeader
}

export default AuthService;
>>>>>>> 479978a42a3900b9d3a156c1c2bd13b23d0c3b1c
