import styled from "@emotion/styled";
import { Flex, Button } from "common/components";
import { http } from "common/services";
import { Dimmer } from "components";
import { useCallback, useEffect } from "react";
import { useQueryClient } from "react-query";
import { loginPopupState } from "state";
import Image from "next/image";
import { useSetAtom } from "jotai";
import { isDevEnv } from "utils";

const Logo = styled.span`
  cursor: pointer;
  font-weight: bold;
  font-size: 28px;
  color: var(--primary);
`;

const fetchAuth = async () => {
  const res = await http.get<{
    success: boolean;
    profileImage?: string;
    nickname?: string;
  }>("/auth");
  if (res.data) window.sessionStorage.setItem("auth", JSON.stringify(res.data));
  return res.data;
};

export const Login = () => {
  const queryClient = useQueryClient();
  const setLoginPopup = useSetAtom(loginPopupState);

  const checkLogin = useCallback(async () => {
    const auth = await fetchAuth();
    if (!auth.success) return;
    queryClient.invalidateQueries();
    queryClient.setQueryData(["auth"], auth);
    setLoginPopup(false);
  }, [queryClient, setLoginPopup]);

  useEffect(() => {
    const listener = (event: MessageEvent) => {
      if (!event) return;
      if (event.origin !== location.origin) return;
      if (event.data !== true) return;
      checkLogin();
    };
    window.addEventListener("message", listener, false);
    return () => window.removeEventListener("message", listener);
  }, [checkLogin, queryClient, setLoginPopup]);

  return (
    <>
      <Dimmer onClick={() => setLoginPopup(false)}>
        <div
          css={{
            display: "flex",
            height: "100%",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <div
            onClick={(e) => e.stopPropagation()}
            css={{
              backgroundColor: "var(--positive)",
              borderRadius: "8px",
              padding: "32px",
              boxShadow: "0px 0px 10px 0px rgba(0,0,0,0.1)",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              justifyContent: "space-around",
              gap: "32px",
            }}
          >
            <Image
              height="32"
              width="128"
              src={"/logo-with-text.svg"}
              alt="logo"
            />
            <Flex column css={{ gap: "24px", alignItems: "center" }}>
              <Flex
                css={{
                  justifyContent: "space-between",
                  "& > *+*": {
                    marginLeft: "24px",
                  },
                }}
              >
                <Button
                  onClick={() =>
                    window.open(
                      "/api/oauth2/authorization/google",
                      undefined,
                      "height=500,width=400,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes"
                    )
                  }
                  css={{ width: "72px", height: "72px" }}
                >
                  <Image
                    height="32"
                    width="32"
                    src={"/google_login.png"}
                    alt="logo"
                  />
                </Button>
                <Button
                  onClick={() =>
                    window.open(
                      "/api/oauth2/authorization/github",
                      undefined,
                      "height=500,width=400,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes"
                    )
                  }
                  css={{
                    width: "72px",
                    height: "72px",
                    backgroundColor: "#161b22",
                  }}
                >
                  <Image
                    height="32"
                    width="32"
                    src={"/github_login.png"}
                    alt="logo"
                  />
                </Button>
                <Button
                  onClick={() =>
                    window.open(
                      "/api/oauth2/authorization/kakao",
                      undefined,
                      "height=500,width=400,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes"
                    )
                  }
                  css={{
                    width: "72px",
                    height: "72px",
                    backgroundColor: "#fee500",
                  }}
                >
                  <Image
                    height="32"
                    width="32"
                    src={"/kakao_login.png"}
                    alt="logo"
                  />
                </Button>
                <Button
                  onClick={() =>
                    window.open(
                      "/api/oauth2/authorization/naver",
                      undefined,
                      "height=500,width=400,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes"
                    )
                  }
                  css={{
                    width: "72px",
                    height: "72px",
                    backgroundColor: "#03cf5d",
                  }}
                >
                  <Image
                    height="32"
                    width="32"
                    src={"/naver_login.png"}
                    alt="logo"
                  />
                </Button>
              </Flex>
              <p>로 로그인 / 회원가입</p>
              {isDevEnv && (
                <Button onClick={checkLogin}>manual login refresh</Button>
              )}
            </Flex>
          </div>
        </div>
      </Dimmer>
    </>
  );
};
