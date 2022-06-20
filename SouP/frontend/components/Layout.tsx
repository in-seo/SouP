import { css, keyframes } from "@emotion/react";
import styled from "@emotion/styled";
import { useMatch } from "hooks/useMatch";
import Link, { LinkProps } from "next/link";
import { useRouter } from "next/router";
import React, { useRef, useEffect } from "react";
import { Dimmer, Header, Media, Portal } from "components";
import { Button } from "common/components";
import useAuth from "hooks/useAuth";
import { loginPopupState, sideBarOpenState } from "state";
import { MdOutlineDarkMode, MdOutlineLightMode } from "react-icons/md";
import { breakpoints } from "utils";
import { WithThemeToggle } from "utils/renderProps";
import { useAtom, useSetAtom } from "jotai";
import { ISideBarProps } from "types";

const PageContainer = styled.div`
  min-height: 100vh;
`;

const BodyContainer = styled.div`
  min-height: 100vh;
  ${breakpoints.greaterThan("md")} {
    margin-left: 240px;
  }
  display: flex;
  flex-direction: row;
  padding-right: 36px;
  padding-left: 36px;
  ${breakpoints.at("sm")} {
    padding-right: 12px;
    padding-left: 12px;
  }
`;

const SlideAnimation = keyframes`
  0% {
    margin-left: -300px
  }
  100% {
    margin-left: 0px
  }
`;

const SideBarContainerWrapper = styled.div<{
  animated?: boolean;
}>`
  display: flex;
  flex-direction: column;
  z-index: 9999;
  background-color: var(--positive);
  position: fixed;
  top: 59px;
  min-width: 240px;
  height: calc(100vh - 59px);
  justify-content: space-between;
  border-right: solid 1px var(--outline);
  padding: 16px;
  ${breakpoints.at("sm")} {
    top: 0;
    height: 100vh;
  }
  ${(animated) =>
    animated &&
    css`
      animation: ${SlideAnimation} 0.1s ease-out;
    `}
`;

const SideBarContentWrapper = styled.ul`
  margin: 0;
  line-height: normal;
  list-style-type: none;
  ${breakpoints.at("sm")} {
    top: 0;
  }
`;

const SideBarContainer = ({
  children,
  animated,
}: {
  children?: React.ReactNode;
  animated?: boolean;
}) => {
  return (
    <SideBarContainerWrapper animated={animated}>
      <SideBarContentWrapper>{children}</SideBarContentWrapper>
      <footer>
        <div css={{ fontSize: "14px", color: "var(--negative2)" }}>
          개인정보처리방침 <br /> © 2022 SouP
        </div>
      </footer>
    </SideBarContainerWrapper>
  );
};

const SideBarLink = styled.a<ISideBarProps>`
  display: block;
  cursor: pointer;
  padding: 10px 12px;
  border-radius: 8px;
  margin-bottom: 4px;
  font-weight: 500;
  ${(props) =>
    props.selected
      ? css`
          font-weight: 700;
          background-color: var(--primarylight);
        `
      : css`
          color: var(--negative2);
          :hover {
            background-color: var(--primarylight2);
          }
        `};
`;

const SideBarElement = ({
  children,
  selected,
  exact,
  authorized,
  ...props
}: ISideBarProps & React.PropsWithChildren<LinkProps>) => {
  const match = useMatch(props.href, exact);
  const setLoginPopup = useSetAtom(loginPopupState);
  const auth = useAuth();

  const LinkWrapper =
    authorized && !auth.success
      ? ({ children }: { children: React.ReactNode }) => (
          <SideBarLink onClick={() => setLoginPopup(true)} selected={match}>
            {children}
          </SideBarLink>
        )
      : ({ children }: { children: React.ReactNode }) => (
          <Link {...props}>
            <SideBarLink href={props.href.toString()} selected={match}>
              {children}
            </SideBarLink>
          </Link>
        );

  return (
    <li>
      <LinkWrapper>{children}</LinkWrapper>
    </li>
  );
};

export const ChildrenContainer = styled.div<{
  width?: number;
}>`
  margin-top: 59px;
  margin-bottom: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  --width: ${({ width }) => width || 1140}px;
  & > .dividing {
    margin-right: -36px;
    margin-left: -36px;
    padding-right: 36px;
    padding-left: 36px;
    width: calc(100% + 72px);
    ${breakpoints.at("sm")} {
      margin-right: -12px;
      margin-left: -12px;
      padding-right: 12px;
      padding-left: 12px;
      width: calc(100% + 24px);
    }
  }
`;

const HeaderIconStyle = css({
  color: "var(--negative2)",
  margin: "-9px",
});

const SideBarNavigation = () => {
  return (
    <>
      <Media at="sm">
        <WithThemeToggle>
          {({ theme, toggleTheme }) => (
            <Button
              icon
              onClick={toggleTheme}
              css={{ width: "36px", marginBottom: "16px" }}
            >
              {theme === "light" ? (
                <MdOutlineLightMode css={HeaderIconStyle} />
              ) : (
                <MdOutlineDarkMode css={HeaderIconStyle} />
              )}
            </Button>
          )}
        </WithThemeToggle>
      </Media>
      <SideBarElement href="/" selected>
        홈
      </SideBarElement>
      <SideBarElement authorized href="/projects/write">
        모집 만들기
      </SideBarElement>
      <SideBarElement href="/projects" exact={false}>
        프로젝트/스터디 찾기
      </SideBarElement>
      <SideBarElement href="/lounge">라운지</SideBarElement>
      <br />
      <SideBarElement authorized href="/profile">
        내 프로필
      </SideBarElement>
      {/* <SideBarElement href="">새소식</SideBarElement>
      <SideBarElement href="">쪽지</SideBarElement> */}
    </>
  );
};

const SideBar = React.memo(
  ({ ...props }: React.ComponentProps<typeof SideBarContainer>) => {
    return (
      <>
        <SideBarContainer {...props}>
          <SideBarNavigation />
        </SideBarContainer>
      </>
    );
  }
);
SideBar.displayName = "SideBar";

const MobileSideBar = React.memo(
  ({ ...props }: {} & React.ComponentProps<typeof SideBarContainer>) => {
    const router = useRouter();

    const [sideBarOpen, setSideBarOpen] = useAtom(sideBarOpenState);

    const initial = useRef(true);
    useEffect(() => {
      if (initial.current) initial.current = false;
      else if (router.pathname) setSideBarOpen(false);
    }, [router.pathname, setSideBarOpen]);

    if (!sideBarOpen) return null;
    return (
      <>
        <Dimmer onClick={() => setSideBarOpen(false)} css={{ zIndex: 9998 }} />
        <Portal at="#portal">
          <SideBarContainer animated {...props}>
            <SideBarNavigation />
          </SideBarContainer>
        </Portal>
      </>
    );
  }
);
MobileSideBar.displayName = "MobileSideBar";

export const Layout = ({ children }: { children: React.ReactNode }) => {
  return (
    <>
      <PageContainer>
        <Header />
        <Media at="sm">
          <MobileSideBar />
        </Media>
        <Media greaterThan="sm">
          <SideBar />
        </Media>
        <BodyContainer>{children}</BodyContainer>
      </PageContainer>
    </>
  );
};
