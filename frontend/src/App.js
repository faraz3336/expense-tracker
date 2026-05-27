import "./index.css";
import "./assets/styles/header.css";
import "./assets/styles/register.css";
import "./assets/styles/user.css";

import { Suspense, lazy } from "react";
import { Routes, Route, Navigate, Outlet } from "react-router-dom";

import Loading from "./components/utils/loading";
import BackendStarting from "./components/BackendStarting";
import AuthService from "./services/auth.service";
import { ThemeContext, useTheme } from "./contexts/ThemeContext.js";

import NewSavedTransaction from "./pages/user/newSavedTransaction";
import SavedTransactions from "./pages/user/savedTransactions";
import EditSavedTransaction from "./pages/user/editSavedTransaction";

const Welcome = lazy(() => import("./pages/welcome"));
const Login = lazy(() => import("./pages/auth/login/login"));
const Register = lazy(() => import("./pages/auth/register/register"));
const UserRegistrationVerfication = lazy(
  () => import("./pages/auth/register/userRegistrationVerification"),
);
const RegistrationSuccess = lazy(
  () => import("./pages/auth/register/registrationSuccessfull"),
);

const Dashboard = lazy(() => import("./pages/user/dashboard"));
const Transactions = lazy(() => import("./pages/user/transactions"));
const NewTransaction = lazy(() => import("./pages/user/newTransaction"));
const EditTransaction = lazy(() => import("./pages/user/editTransaction"));

const ForgotPasswordEmailVerfication = lazy(
  () => import("./pages/auth/forgotpassword/forgotPasswordEmailVerification"),
);
const ForgotPasswordCodeVerification = lazy(
  () => import("./pages/auth/forgotpassword/forgotPasswordCodeVerification"),
);
const ForgotPasswordChangePassword = lazy(
  () => import("./pages/auth/forgotpassword/changePassword"),
);

const UnAuthorizedAccessPage = lazy(() => import("./pages/auth/unAuthorized"));
const NotFoundPage = lazy(() => import("./pages/auth/notFound"));

const AdminTransactionsManagement = lazy(
  () => import("./pages/admin/transactions"),
);
const AdminUsersManagement = lazy(() => import("./pages/admin/users"));
const AdminCategoriesManagement = lazy(
  () => import("./pages/admin/categories"),
);
const NewCategory = lazy(() => import("./pages/admin/newCategory"));
const EditCategory = lazy(() => import("./pages/admin/editCategory"));
const AdminProfile = lazy(() => import("./pages/admin/adminProfile"));

const UserProfile = lazy(() => import("./pages/user/userProfile"));
const UserStatistics = lazy(() => import("./pages/user/statistics"));

const ProtectedRoute = ({ isAllowed, redirectPath = "/unauthorized" }) => {
  if (!isAllowed) {
    return <Navigate to={redirectPath} replace />;
  }

  return <Outlet />;
};

function App() {
  const [isDarkMode, toggleTheme] = useTheme();
  const user = AuthService.getCurrentUser();

  return (
    <Suspense fallback={<LoadingSpinner />}>
      <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
        <BackendStarting>
          <RoutesWrapper isDarkMode={isDarkMode}>
            <Routes>
              <Route path="/" element={<Welcome />} />
              <Route path="/auth/login" element={<Login />} />
              <Route path="/auth/register" element={<Register />} />
              <Route
                path="/auth/userRegistrationVerfication/:email"
                element={<UserRegistrationVerfication />}
              />
              <Route
                path="/auth/success-registration"
                element={<RegistrationSuccess />}
              />
              <Route
                path="/auth/forgetpassword/verifyEmail"
                element={<ForgotPasswordEmailVerfication />}
              />
              <Route
                path="/auth/forgotPassword/verifyAccount/:email"
                element={<ForgotPasswordCodeVerification />}
              />
              <Route
                path="/auth/forgotPassword/resetPassword/:email"
                element={<ForgotPasswordChangePassword />}
              />

              <Route
                element={
                  <ProtectedRoute
                    isAllowed={user && user.roles?.includes("ROLE_USER")}
                  />
                }
              >
                <Route path="/user/dashboard" element={<Dashboard />} />
                <Route path="/user/transactions" element={<Transactions />} />
                <Route path="/user/newTransaction" element={<NewTransaction />} />
                <Route
                  path="/user/editTransaction/:transactionId"
                  element={<EditTransaction />}
                />
                <Route
                  path="/user/savedTransactions"
                  element={<SavedTransactions />}
                />
                <Route
                  path="/user/savedTransactions/new"
                  element={<NewSavedTransaction />}
                />
                <Route
                  path="/user/editSavedTransaction/:transactionId"
                  element={<EditSavedTransaction />}
                />
                <Route path="/user/statistics" element={<UserStatistics />} />
                <Route path="/user/settings" element={<UserProfile />} />
              </Route>

              <Route
                element={
                  <ProtectedRoute
                    isAllowed={user && user.roles?.includes("ROLE_ADMIN")}
                  />
                }
              >
                <Route
                  path="/admin/transactions"
                  element={<AdminTransactionsManagement />}
                />
                <Route path="/admin/users" element={<AdminUsersManagement />} />
                <Route
                  path="/admin/categories"
                  element={<AdminCategoriesManagement />}
                />
                <Route path="/admin/newCategory" element={<NewCategory />} />
                <Route
                  path="/admin/editCategory/:categoryId"
                  element={<EditCategory />}
                />
                <Route path="/admin/settings" element={<AdminProfile />} />
              </Route>

              <Route path="/unauthorized" element={<UnAuthorizedAccessPage />} />
              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </RoutesWrapper>
        </BackendStarting>
      </ThemeContext.Provider>
    </Suspense>
  );
}

function RoutesWrapper({ children, isDarkMode }) {
  return <div className={isDarkMode ? "dark" : "light"}>{children}</div>;
}

function LoadingSpinner() {
  return (
    <div style={{ width: "100%", height: "100vh" }}>
      <Loading />
    </div>
  );
}

export default App;
