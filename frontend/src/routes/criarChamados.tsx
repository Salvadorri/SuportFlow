import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import CriarChamadoPage from "../pages/criarChamado";

export const CriarChamadoRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/criar-chamado",
  component: CriarChamadoPage,
});
