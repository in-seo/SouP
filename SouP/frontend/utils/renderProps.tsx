import { useTheme as useNextTheme } from "next-themes";

const themeTransition = (ms: number) => {
  const css = document.createElement("style");
  css.appendChild(
    document.createTextNode(
      `* { transition-duration: 250ms; transition-property: background-color, outline-color, border-color; -webkit-transition-duration: 250ms; -webkit-transition-property: background-color, outline-color, border-color; }`
    )
  );
  document.head.appendChild(css);
  return () => {
    (() => window.getComputedStyle(document.body))();
    setTimeout(() => {
      document.head.removeChild(css);
    }, ms);
  };
};

export const WithThemeToggle = ({
  children,
}: {
  children(props: { theme: string; toggleTheme: () => void }): JSX.Element;
}) => {
  const { theme, setTheme } = useNextTheme();
  const toggleTheme = () => {
    const removeAnimation = themeTransition(250);
    setTheme(theme === "light" ? "dark" : "light");
    removeAnimation();
  };

  return children({ theme, toggleTheme });
};
