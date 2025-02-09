import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import CriarChamadoPage from "../pages/criarChamado";

export const CriarChamadoRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/criar-chamado", // Define o caminho da rota
  component: CriarChamadoPage, // Componente a ser renderizado
});
