import { SectionBody } from "common/components";
import Link from "next/link";
import { ChildrenContainer } from "./Layout";

const STATUS = {
  404: {
    message: "Not Found",
    description: "존재하지 않는 페이지에요 T-T",
  },
  500: {
    message: "Internal Server Error",
    description: "알 수 없는 문제가 발생했어요 T-T",
  },
};

export const Error = ({ status }: { status: keyof typeof STATUS }) => {
  return (
    <ChildrenContainer>
      <SectionBody>
        <div
          css={{
            marginTop: "24px",
            color: "var(--negative2)",
          }}
        >
          <div
            css={{
              lineHeight: 1,
              fontWeight: 700,
              marginBottom: "12px",
              color: "var(--positive)",
              backgroundColor: "var(--primary)",
              padding: "8px 12px",
              borderRadius: "8px",
            }}
          >
            <h1
              css={{
                fontSize: "60px",
              }}
            >
              {status}
            </h1>
          </div>
          <p>{STATUS[status].message}</p>
          <br />
          <p>{STATUS[status].description}</p>
          <Link href="/">
            <a>{"메인으로 돌아가기 >>"}</a>
          </Link>
        </div>
      </SectionBody>
    </ChildrenContainer>
  );
};
