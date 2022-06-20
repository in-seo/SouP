import { MutableRefObject } from "react";
import { Flex } from ".";
import SwiperType from "swiper";
import React from "react";

export const CarouselPagination = React.memo(
  ({
    current,
    end,
    swiperRef,
  }: {
    current: number;
    end: number;
    onClick?: (i: number) => void;
    className?: string | undefined;
    swiperRef: MutableRefObject<SwiperType | null>;
  }) => {
    return (
      <Flex
        css={{
          alignItems: "center",
        }}
      >
        <Flex css={{ gap: "8px", marginRight: "12px" }}>
          {Array(end)
            .fill(undefined)
            .map((_, i) => (
              <span
                key={i}
                onClick={() => swiperRef?.current?.slideTo(i + 1)}
                css={{
                  cursor: "pointer",
                  display: "inline-block",
                  width: "6px",
                  height: "6px",
                  borderRadius: "3px",
                  backgroundColor:
                    i === current ? "var(--negative2)" : "var(--outline)",
                }}
              />
            ))}
        </Flex>
        <Flex
          css={{
            width: "36px",
            height: "18px",
            borderRadius: "9px",
            backgroundColor: "var(--background)",
            border: "1px solid var(--outline)",
            color: "var(--negative2)",
            fontSize: "11px",
            justifyContent: "center",
            alignItems: "center",
            lineHeight: "initial",
          }}
        >
          {current + 1} / {end}
        </Flex>
      </Flex>
    );
  }
);

CarouselPagination.displayName = "CarouselPagination";
