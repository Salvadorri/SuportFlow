import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import CodigoDeVerificacaoPage from "../pages/codigoDeVerificacao";

export const CodigoDeVerificacaoRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/verify-code", // Define o caminho da rota
  component: CodigoDeVerificacaoPage, // Componente a ser renderizado
});
