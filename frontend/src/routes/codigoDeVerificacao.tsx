import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import CodigoDeVerificacaoPage from "../pages/codigoDeVerificacao";

export const CodigoDeVerificacaoRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/verify-code",
  component: CodigoDeVerificacaoPage,
});
