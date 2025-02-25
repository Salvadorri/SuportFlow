import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import meuschatsclientesPage from "../pages/meuschatsclientes";

export const meuschatsclientesRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/meuschatsclientes", // Define o caminho da rota
  component: meuschatsclientesPage, // Componente a ser renderizado
});