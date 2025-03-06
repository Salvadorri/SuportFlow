import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import Edit_VerChamadoPage from "../pages/edit_VerChamado";

export const Edit_VerChamado = createRoute({
  getParentRoute: () => rootRoute,
  path: "/editar-Chamado",
  component: Edit_VerChamadoPage,
});