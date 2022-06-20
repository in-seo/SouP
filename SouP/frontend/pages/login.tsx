import { useAtom, useAtomValue } from "jotai";
import { GetServerSideProps, NextPage } from "next";
import { useRouter } from "next/router";
import { useEffect, useLayoutEffect } from "react";
import { loginPopupState } from "state";
import { useHydrateAtoms } from "jotai/utils";
import useAuth from "hooks/useAuth";
import useClientRender from "hooks/useClientRender";

const Login: NextPage = () => {
  useHydrateAtoms([[loginPopupState, true] as any]);
  const [loginPopup, setLoginPopup] = useAtom(loginPopupState);
  const isClientRender = useClientRender();

  const router = useRouter();
  const auth = useAuth();

  const { redirect } = router.query as {
    redirect: string;
  };

  useLayoutEffect(() => {
    setLoginPopup(true);
    return () => setLoginPopup(false);
  }, [setLoginPopup]);

  useLayoutEffect(() => {
    if (isClientRender && auth.success) {
      router.push(redirect);
    } else if (isClientRender && !loginPopup) router.push("/");
  }, [
    auth.success,
    isClientRender,
    loginPopup,
    redirect,
    router,
    setLoginPopup,
  ]);

  return <></>;
};

export default Login;
