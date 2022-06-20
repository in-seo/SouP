import { css, Theme } from "@emotion/react";
import { CSSInterpolation } from "@emotion/serialize";
import styled from "@emotion/styled";
import { Flex } from "common/components";
import { breakpoints } from "utils";

export const SectionHeader = Object.assign(
  styled.div`
    display: flex;
    flex-direction: column;
    margin-top: 36px;
    ${breakpoints.at("sm")} {
      margin-top: 24px;
    }
    margin-bottom: 24px;
    width: 100%;
    max-width: 1140px;
  `,
  {
    Title: styled.div`
      display: flex;
      font-weight: bold;
      font-size: 20px;
    `,
    Description: styled.div`
      display: flex;
      flex-direction: column;
      font-size: 12px;
    `,
  }
);

const SectionBodyAltContainer = styled.section`
  display: flex;
  width: 100%;
  justify-content: center;
  background-color: var(--positive);
  border-top: 1px solid var(--outline);
  border-bottom: 1px solid var(--outline);
`;

export const SectionBodyAlt = ({
  children,
  className,
}: {
  column?: boolean;
  inline?: boolean;
  children?: React.ReactNode;
  className?: any;
}) => {
  return (
    <SectionBodyAltContainer
      css={{
        marginBottom: "24px",
        paddingTop: "12px",
        paddingBottom: "12px",
      }}
      className={`dividing ${className}`}
    >
      <div
        css={{
          display: "flex",
          width: "100%",
          maxWidth: "1140px",
        }}
      >
        <div
          css={{
            flex: "0 1 var(--width)",
            width: 0,
          }}
        >
          {children}
        </div>
      </div>
    </SectionBodyAltContainer>
  );
};

const SectionBodyContainer = styled.section`
  display: flex;
  width: 100%;
  max-width: 1140px;
`;

export const SectionBody = ({
  children,
  className,
}: {
  column?: boolean;
  inline?: boolean;
  children?: React.ReactNode;
  className?: any;
}) => {
  return (
    <SectionBodyContainer
      css={{
        marginBottom: "24px",
      }}
    >
      <div
        css={[
          {
            flex: "0 1 var(--width)",
            width: 0,
          },
          className,
        ]}
      >
        {children}
      </div>
    </SectionBodyContainer>
  );
};
