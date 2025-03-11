import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import TrocarSenhaPage from "../pages/trocarSenha";

export const TrocarSenhaRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/change-password",
  component: TrocarSenhaPage,
});
