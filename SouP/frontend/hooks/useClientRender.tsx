import { useLayoutEffect, useState } from "react";

export default function useClientRender() {
  const [state, setState] = useState(false);
  useLayoutEffect(() => {
    setState(true);
  }, []);
  return state;
}
