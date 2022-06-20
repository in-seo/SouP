import { css, Theme } from "@emotion/react";
import styled from "@emotion/styled";
import { breakpoints } from "utils";

const BoxVariant = (theme: Theme) => ({
  primary: css`
    border: 1px solid var(--primary);
    background: var(--positive);
    color: var(--primary);
  `,
  background: css`
    border: 1px solid var(--outline);
    background: var(--background);
  `,
  transparent: css``,
});

export const Flex = styled.div<{
  column?: boolean;
  inline?: boolean;
}>`
  ${({ inline }) =>
    inline
      ? css`
          display: inline-flex;
        `
      : css`
          display: flex;
        `}
  ${({ column }) =>
    column &&
    css`
      flex-direction: column;
    `}
`;

const fullspanStyle = css`
  border-radius: 0;
  padding-left: 12px;
  margin-left: -12px;
  padding-right: 12px;
  margin-right: -12px;
  border-left: 0px;
  border-right: 0px;
`;

export const Box = styled.div<{
  variant?: keyof ReturnType<typeof BoxVariant>;
  column?: boolean;
  responsive?: boolean;
  fullspan?: boolean;
}>`
  border-radius: 8px;
  display: flex;
  padding: 12px;
  ${({ fullspan }) => fullspan && fullspanStyle}
  ${({ column }) =>
    column &&
    css`
      flex-direction: column;
    `}
  ${({ responsive }) =>
    responsive &&
    css({
      [breakpoints.at("sm")]: fullspanStyle,
    })}
  ${({ variant, theme }) =>
    variant
      ? BoxVariant(theme)[variant]
      : css`
          border: 1px solid var(--outline);
          background-color: var(--positive);
        `}
`;
