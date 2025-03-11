import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import RecuperarSenha from "../pages/recuperarSenha";

export const RecuperarSenhaRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/restore-password",
  component: RecuperarSenha,
});