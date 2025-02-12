import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import Edit_VerChamadoPage from "../pages/edit_VerChamado";

export const Edit_VerChamado = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/editar-Chamado", // Define o caminho da rota
  component: Edit_VerChamadoPage, // Componente a ser renderizado
});