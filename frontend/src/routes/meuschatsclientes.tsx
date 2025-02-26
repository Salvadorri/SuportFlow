import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import meuschatsclientesPage from "../pages/meuschatsclientes";

export const meuschatsclientesRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/meuschatsclientes",
  component: meuschatsclientesPage,
});
