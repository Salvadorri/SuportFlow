import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import ChamadoHistoricoPage from "../pages/chamadosHistorico";

export const ChamadoHistoricoRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/chamados-historico", // Define o caminho da rota
  component: ChamadoHistoricoPage, // Componente a ser renderizado
});
