import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import RecuperarSenha from "../pages/recuperarSenha";

export const RecuperarSenhaRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/restore-password", // Define o caminho da rota
  component: RecuperarSenha, // Componente a ser renderizado
});
