import { LinkProps } from "next/link";
import { useRouter } from "next/router";
import { useCallback, useEffect, useState } from "react";

export function useToggle(initial = false): [boolean, (set?: boolean) => void] {
  const [state, setState] = useState(initial);

  const toggle = useCallback(
    (set?: boolean) =>
      setTimeout(
        () => setState((state) => (set !== undefined ? set : !state)),
        0
      ),
    []
  );
  return [state, toggle];
}
