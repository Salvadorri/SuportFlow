import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import dashboardClientePage from "../pages/dashboardCliente";

export const dashboardClienteRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/dashboardCliente",
  component: dashboardClientePage,
});