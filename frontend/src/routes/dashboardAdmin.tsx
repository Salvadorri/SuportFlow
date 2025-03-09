import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import dashboardAdminPage from "../pages/dashboardAdmin";

export const dashboardAdminRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/dashboardAdmin",
  component: dashboardAdminPage,
});