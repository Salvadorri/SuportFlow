import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import ChamadosPage from "../pages/chamados";

export const ChamadosRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/chamados",
  component: ChamadosPage,
});