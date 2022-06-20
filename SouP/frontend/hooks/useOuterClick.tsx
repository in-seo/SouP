import { useEffect, useRef } from "react";

export const useOuterClick = <T extends HTMLElement>(
  handler: (event: Event) => void
) => {
  const ref = useRef<T>(null);

  useEffect(() => {
    const listener = (event: Event) => {
      const el = ref?.current;
      if (!el || el.contains((event?.target as Node) || null)) {
        return;
      }

      handler(event); // Call the handler only if the click is outside of the element passed.
    };
    document.addEventListener("mouseup", listener);
    document.addEventListener("touchend", listener);
    return () => {
      document.removeEventListener("mouseup", listener);
      document.removeEventListener("touchend", listener);
    };
  });

  return ref;
};
