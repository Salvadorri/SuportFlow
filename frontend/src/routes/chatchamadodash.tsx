import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import chatchamadodashPage from "../pages/chatchamadodash";

export const chatchamadodashRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/chat-chamado",
  component: chatchamadodashPage,
});