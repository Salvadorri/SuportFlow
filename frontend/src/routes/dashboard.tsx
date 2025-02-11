import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import DashboardPage from "../pages/dashboard";

export const dashboardRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/dashboard", // Define o caminho da rota
  component: DashboardPage, // Componente a ser renderizado
});
