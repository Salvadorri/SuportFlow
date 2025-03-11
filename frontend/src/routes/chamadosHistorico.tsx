import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import ChamadoHistoricoPage from "../pages/chamadosHistorico";

export const ChamadoHistoricoRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/chamados-historico",
  component: ChamadoHistoricoPage,
});
