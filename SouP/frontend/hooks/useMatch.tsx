import { LinkProps } from "next/link";
import { useRouter } from "next/router";
import { useEffect, useLayoutEffect, useState } from "react";

const routeStore = new Set<string>();

export function useMatch(href: LinkProps["href"], exact = true) {
  const [match, setMatch] = useState(false);
  const router = useRouter();

  const hrefString = href.toString();
  routeStore.add(hrefString);

  useEffect(() => {
    // cleanup
    return () => routeStore.clear();
  });

  const pathname = router.asPath.split("?")[0];

  useLayoutEffect(() => {
    if (href === pathname) {
      // true: all case full match
      setMatch(true);
    } else if (exact) {
      // false: exact but fail
      setMatch(false);
    } else if (routeStore.has(pathname)) {
      // false: !exact but already found full match
      setMatch(false);
    } else if (pathname.startsWith(hrefString)) {
      // true: !exact and partial only match
      setMatch(true);
    } else {
      // false: other case
      setMatch(false);
    }
  }, [exact, href, hrefString, pathname]);

  return match;
}
