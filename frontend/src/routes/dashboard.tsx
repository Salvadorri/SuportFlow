// src/routes/dashboard.tsx
import { createRoute, redirect } from "@tanstack/react-router";
import { getClientToken, getUserToken } from "../api/login-jwt";
import DashboardAdmin from "../components/dashboard/dashboardAdmin"; // Import the different dashboard components
import DashboardCliente from "../components/dashboard/dashboardCliente";
import DashboardAtendente from "../components/dashboard/index";
import { rootRoute } from "./__root";

const requireAuth = () => {
  const userToken = getUserToken();
  const clientToken = getClientToken();

  if (!userToken && !clientToken) {
    throw redirect({ to: "/" });
  }
  return true;
};

const requireAdmin = () => {
  if (!requireAuth()) return false;
  const userRolesString = localStorage.getItem("userRoles");

  if (
    !userRolesString ||
    userRolesString === "undefined" ||
    userRolesString === "null"
  ) {
    console.warn(
      "User roles string is missing or invalid. Redirecting to login."
    );
    throw redirect({ to: "/" });
  }

  const allowedRoles = ["ADMIN"];

  try {
    const userRolesArray = JSON.parse(userRolesString) as string[];

    const hasPermission = userRolesArray.some((role) =>
      allowedRoles.includes(role)
    );

    if (!hasPermission) {
      console.warn("User does not have required roles. Redirecting to login.");
      throw redirect({ to: "/" });
    }
  } catch (error) {
    console.error("Error parsing user roles:", error);
    throw redirect({ to: "/" });
  }
  return true;
};

const requireAtendente = () => {
  if (!requireAuth()) return false;
  const userRolesString = localStorage.getItem("userRoles");

  if (
    !userRolesString ||
    userRolesString === "undefined" ||
    userRolesString === "null"
  ) {
    console.warn("User roles missing. Redirecting...");
    throw redirect({ to: "/" });
  }

  // Allow admins to access atendente dashboard
  const allowedRoles = ["ATENDENTE", "ADMIN"];

  try {
    const userRoles = JSON.parse(userRolesString) as string[];
    if (!userRoles.some((role) => allowedRoles.includes(role))) {
      console.warn("Unauthorized access to atendente dashboard");
      throw redirect({ to: "/" });
    }
  } catch (err) {
    console.error("Error parsing roles:", err);
    throw redirect({ to: "/" });
  }
  return true;
};

const requireCliente = () => {
  if (!requireAuth()) return false;
  const clientToken = getClientToken();

  if (!clientToken) {
    console.warn(
      "User roles string is missing or invalid. Redirecting to login."
    );
    throw redirect({ to: "/" });
  }
  return true;
};

export const dashboardAdminRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/dashboard/admin",
  component: DashboardAdmin,
  beforeLoad: () => {
    requireAdmin();
  },
});

export const dashboardRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/dashboard",
  component: DashboardAtendente,
  beforeLoad: () => requireAtendente(), // ADICIONE ESTE HOOK PARA USAR requireAtendente
});

export const dashboardClienteRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/dashboard/cliente",
  component: DashboardCliente,
  beforeLoad: () => {
    requireCliente();
  },
});
