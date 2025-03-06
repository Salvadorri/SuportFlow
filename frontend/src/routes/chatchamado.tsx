import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import chatchamadoPage from "../pages/chatchamado";

export const chatchamadoRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/chatchamado",
  component: chatchamadoPage,
});