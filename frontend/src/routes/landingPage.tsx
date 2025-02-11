import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import LandingPage from "../pages/landingPage";

export const landingRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/", // Define o caminho da rota
  component: LandingPage, // Componente a ser renderizado
});
