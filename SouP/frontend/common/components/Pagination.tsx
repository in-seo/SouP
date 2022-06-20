import { css } from "@emotion/react";
import styled from "@emotion/styled";
import React from "react";
import { useMemo } from "react";
import { Flex } from "./Box";

const Container = styled(Flex)`
  justify-content: center;
  align-items: center;
  padding: 4px;
  gap: 2px;
`;

const Item = styled.button<{
  current?: boolean;
}>`
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--negative2);
  height: 24px;
  padding: 4px 6px;
  min-width: 24px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  svg {
    font-size: 16px;
  }
  :disabled {
    color: #d3d5d9;
  }
  border-width: 0px;
  ${({ current }) =>
    current &&
    css`
      color: var(--negative);
      background-color: var(--positive);
      border: 1px solid var(--outline);
    `}
`;

export const Pagination = React.memo(
  ({
    current,
    end,
    onClick,
    className,
  }: {
    current: number;
    end: number;
    onClick: (i: number) => void;
    className?: string | undefined;
  }) => {
    const pages = useMemo(
      () =>
        Array.from(Array(9).keys())
          .slice(0, end ? (end > 9 ? 9 : end) : 9)
          .map(
            (x) =>
              x +
              (end > 9
                ? Math.max(0, current - 5) + Math.min(0, end - current - 4) + 1
                : 1)
          ),
      [current, end]
    );

    return (
      <Container className={className}>
        {pages.map((item) => (
          <Item
            onClick={() => onClick(item)}
            key={item}
            current={current === item}
          >
            {item}
          </Item>
        ))}
      </Container>
    );
  }
);

Pagination.displayName = "Pagination";
