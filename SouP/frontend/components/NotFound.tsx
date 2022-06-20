import { useRouter } from "next/router";
import Error404 from "pages/404";
import { useLayoutEffect } from "react";

export const NotFound = () => {
  const router = useRouter();
  useLayoutEffect(() => {
    router.push("/404", router.asPath, { shallow: true });
  }, [router]);
  return <Error404 />;
};
