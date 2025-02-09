import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import ChamadosPage from "../pages/chamados";

export const ChamadosRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/chamados", // Define o caminho da rota
  component: ChamadosPage, // Componente a ser renderizado
});
