import { Interpolation, keyframes, Theme } from "@emotion/react";
import { useTheme } from "next-themes";
import React from "react";
import Portal from "./Portal";

const FadeInAnimation = keyframes`
  0% {
    opacity: 0
  }
  100% {
    opacity: 1
  }
`;

export const Dimmer = React.memo(
  ({ ...props }: React.ComponentProps<"div"> & {}) => {
    const { theme } = useTheme();
    return (
      <Portal at={"#portal"}>
        <div
          css={{
            position: "fixed",
            top: 0,
            left: 0,
            backdropFilter: "blur(8px)",
            backgroundColor:
              theme === "light"
                ? "rgba(255, 255, 255, 0.5)"
                : "rgba(0, 0, 0, 0.5)",
            width: "100vw",
            height: "100vh",
            zIndex: 99999,
            animation: `${FadeInAnimation} 0.25s ease-in-out`,
          }}
          {...props}
        />
      </Portal>
    );
  }
);
Dimmer.displayName = "Dimmer";
