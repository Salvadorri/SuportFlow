import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import TrocarSenhaPage from "../pages/trocarSenha";

export const TrocarSenhaRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/change-password", // Define o caminho da rota
  component: TrocarSenhaPage, // Componente a ser renderizado
});
