import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import LandingPage from "../pages/landingPage";

export const landingRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/",
  component: LandingPage,
});
