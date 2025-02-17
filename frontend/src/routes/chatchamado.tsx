import { Route } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import chatchamadoPage from "../pages/chatchamado";

export const chatchamadoRoute = new Route({
  getParentRoute: () => rootRoute, // Define a rota pai (rootRoute)
  path: "/chatchamado", // Define o caminho da rota
  component: chatchamadoPage, // Componente a ser renderizado
});