import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import DashboardPage from "../pages/dashboard";

export const dashboardRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/dashboard",
  component: DashboardPage,
});
