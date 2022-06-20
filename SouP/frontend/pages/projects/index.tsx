import {
  Box,
  Label,
  Button,
  Flex,
  ProfilePlaceholder,
  SectionHeader,
  SectionBody,
  SectionBodyAlt,
} from "common/components";
import { GetServerSideProps, NextPage } from "next";
import Image from "next/image";
import { useRouter } from "next/router";
import { dehydrate, QueryClient, useQuery } from "react-query";
import { http } from "common/services";
import { Pagination } from "common/components/Pagination";
import { injectSession, SourceDictionary, timeDiffString } from "utils";
import { useEffect, useLayoutEffect, useMemo, useState } from "react";
import { keyframes } from "@emotion/react";
import { ChildrenContainer } from "components";
import React from "react";
import {
  MdOutlineCheck,
  MdOutlineClose,
  MdOutlineLabel,
  MdOutlineSearch,
} from "react-icons/md";
import { useToggle } from "hooks/useToggle";
import { useCallback } from "react";
import useClientRender from "hooks/useClientRender";
import { ellipsis } from "polished";
import { IFeaturedItem, IPageable, IPost } from "types";
import {
  getDisplayColor,
  getDisplayTag,
  ITag,
  ITagCategory,
  TagGroup,
  TagList,
} from "utils/tagDictionary";
import Link from "next/link";
import { atom, useAtom } from "jotai";

const Post = ({ image, post }: { image?: boolean; post: IPost }) => {
  const timeString = useMemo(() => timeDiffString(post.date), [post]);

  return (
    <Box responsive column>
      <div
        css={{
          height: "100%",
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <div
          css={{
            width: "100%",
            flexWrap: "nowrap",
            boxSizing: "border-box",
            height: "100%",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
            overflow: "hidden",
            lineHeight: "1.2em",
          }}
        >
          <Link href={`/projects/${post.id}`}>
            <a>
              <Flex
                css={{
                  alignItems: "center",
                  fontSize: "14px",
                  marginBottom: "16px",
                  gap: "8px",
                }}
              >
                <ProfilePlaceholder value={post.userName} size={24} />
                <span>
                  {post.userName} · {timeString} · 조회 {post.views} · 스크랩{" "}
                  {post.fav}
                </span>
              </Flex>
              <div
                css={{
                  fontWeight: "700",
                  fontSize: "18px",
                  overflow: "hidden",
                  display: "-webkit-box",
                  WebkitBoxOrient: "vertical",
                  WebkitLineClamp: 2,
                  marginBottom: "12px",
                }}
              >
                {post.postName}
              </div>
              <div
                css={{
                  overflow: "hidden",
                  fontSize: "16px",
                  lineHeight: 1.5,
                  display: "-webkit-box",
                  WebkitBoxOrient: "vertical",
                  WebkitLineClamp: 2,
                  marginBottom: "16px",
                }}
              >
                {post.content}
              </div>
            </a>
          </Link>
        </div>
        {image ? (
          <div
            css={{
              boxSizing: "border-box",
              marginLeft: "16px",
              borderRadius: "4px",
              overflow: "hidden",
              position: "relative",
              flex: "0 0 auto",
              background: "var(--outline)",
              width: "120px",
            }}
          >
            <Image
              css={{ objectFit: "cover" }}
              src="https://i.imgur.com/tvzwhsF.png"
              alt="thumb"
              layout="fill"
            />
          </div>
        ) : (
          <></>
        )}
      </div>
      <div
        css={{
          display: "flex",
          flexDirection: "row",
          marginTop: 0,
          justifyContent: "space-between",
          alignItems: "center",
          lineHeight: "normal",
        }}
      >
        <div
          css={{
            fontWeight: 500,
            display: "flex",
            "& > *+*": { marginLeft: "8px" },
          }}
        >
          <Label
            css={{
              backgroundColor: "var(--positive1)",
            }}
            variant="background"
            size="small"
          >
            {SourceDictionary[post.source]}
          </Label>
          {post.stacks.map((stack) => (
            <Label
              key={stack}
              css={{ backgroundColor: "var(--positive1)" }}
              variant="background"
              size="small"
            >
              <div
                css={{
                  width: "6px",
                  height: "6px",
                  borderRadius: "3px",
                  backgroundColor: getDisplayColor(stack),
                  marginRight: "6px",
                }}
              />
              {getDisplayTag(stack)}
            </Label>
          ))}
        </div>
      </div>
    </Box>
  );
};

const MarqueeAnimation = keyframes`
  0% {
    transform: translate3d(0, 0, 0);
  }
  100% {
    transform: translate3d(-100%, 0, 0);
  }
`;

const ts = {
  height: "36px",
  overflow: "hidden",
  flex: "0 0 auto",
  backgroundColor: "var(--positive1)",
  border: 0,
};

const Test = React.forwardRef<HTMLDivElement>((_, ref) => {
  const { addFilter } = useFilter();
  const shuffle = useMemo(
    () => TagList.slice().sort(() => Math.random() - 0.5),
    []
  );

  if (!useClientRender()) return null;
  return (
    <span
      css={{
        display: "inline-flex",
        animation: `${MarqueeAnimation} ${
          TagList.length * 15
        }s linear infinite`,
        "& > *": { marginRight: "12px" },
      }}
      ref={ref}
    >
      {shuffle.map((stack, i) => (
        <Button onClick={() => addFilter(stack)} key={stack} css={ts}>
          {getDisplayTag(stack)}
        </Button>
      ))}
    </span>
  );
});
Test.displayName = "test";

const Slider = ({ toggle }: { toggle: (set?: boolean) => void }) => {
  const scrollRef = React.useRef<HTMLDivElement | null>(null);
  const [position, setPosition] = React.useState<number>(0);

  const backupHeight = position;

  // when dom loaded, measure target width and scroll
  const contentRef = useCallback(
    (node) => {
      if (node !== null) {
        setPosition(node.offsetWidth);
        if (scrollRef.current) scrollRef.current.scrollLeft = backupHeight;
      }
    },
    [backupHeight]
  );

  // event listener: on mouse wheel scroll
  const onWheel = React.useCallback((e: WheelEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (!scrollRef.current) return;
    const container = scrollRef.current;
    const containerScrollPosition = scrollRef.current.scrollLeft;
    container.scrollTo({
      top: 0,
      left: containerScrollPosition + e.deltaY * 0.25,
    });
  }, []);

  // attach onWheel to scrollRef
  // why: https://github.com/facebook/react/issues/14856
  useEffect(() => {
    if (!scrollRef.current) return;
    const ref = scrollRef.current;
    ref.addEventListener("wheel", onWheel);
    return () => ref.removeEventListener("wheel", onWheel);
  }, [onWheel]);

  // event listener: on any virtual scroll
  const onScroll = React.useCallback(() => {
    if (scrollRef.current) {
      const scroll = scrollRef.current.scrollLeft;
      if (scroll < backupHeight || scroll >= backupHeight + position) {
        scrollRef.current.scrollTo({
          top: 0,
          left: backupHeight + (scroll % position),
          behavior: "auto",
        });
      }
    }
  }, [backupHeight, position]);

  return (
    <>
      <Flex
        ref={scrollRef}
        onScroll={onScroll}
        css={{
          marginLeft: "12px",
          marginRight: "-12px",
          overflow: "auto",
          paddingLeft: 0,
          paddingRight: 0,
          position: "relative",
          "& > *": {
            whiteSpace: "nowrap",
          },
          display: "inline-flex",
          msOverflowStyle: "none" /* IE and Edge */,
          scrollbarWidth: "none" /* Firefox */,
          "::-webkit-scrollbar": {
            display: "none",
          },
          ":hover": {
            "*": {
              animationPlayState: "paused",
            },
          },
        }}
      >
        <Test />
        <Test ref={contentRef} />
        <Test />
        <Test />
      </Flex>
      <div css={{ position: "absolute" }}>
        <Button
          variant="primary-outlined"
          css={{
            height: "36px",
            width: "36px",
            padding: 0,
            backgroundColor: "var(--primarylight2)",
          }}
          onClick={() => toggle()}
        >
          <MdOutlineSearch css={{ fontSize: "18px" }} />
        </Button>
      </div>
    </>
  );
};

const useSearchMenu = (initial: ITagCategory) => {
  const [state, setState] = useState<ITagCategory>(initial);
  const currentMenu = useMemo(
    () => TagGroup.find((menu) => menu.key === state)!,
    [state]
  );

  return [currentMenu, setState] as [typeof currentMenu, typeof setState];
};

const filterState = atom<ITag[]>([]);

function isValidTag(arg: any): arg is ITag {
  return TagList.includes(arg);
}

const useFilter = () => {
  const [filter, setFilter] = useAtom(filterState);
  const resetFilter = useCallback(() => setFilter([]), [setFilter]);
  const router = useRouter();
  const addFilter = useCallback(
    (x: string, shallow?: boolean) => {
      if (!isValidTag(x)) return;
      const result = [...filter, x];
      if (filter.length >= 3 || filter.includes(x)) return;
      setFilter(result);
      console.log("push");
      router.push(
        {
          query: {
            ...router.query,
            stacks: result.join(","),
          },
        },
        undefined,
        {
          shallow,
        }
      );
    },
    [filter, router, setFilter]
  );
  useLayoutEffect(() => {
    let stacks = router.query["stacks"];
    if (!stacks) return;
    if (Array.isArray(stacks)) stacks = stacks.join(",");
    console.log("stacks:", stacks);
    setFilter(stacks.split(",").filter((stack) => isValidTag(stack)) as ITag[]);
  }, [router.query, setFilter]);
  const removeFilter = useCallback(
    (i, shallow?: boolean) => {
      const result = [...filter.slice(0, i), ...filter.slice(i + 1)];
      setFilter(result);
      router.push(
        {
          query: {
            ...router.query,
            stacks: result,
          },
        },
        undefined,
        {
          shallow,
        }
      );
    },
    [filter, router, setFilter]
  );
  return { filter, resetFilter, addFilter, removeFilter };
};

const Search = ({ toggle }: { toggle: (set?: boolean) => void }) => {
  const [currentMenu, setCurrentMenu] = useSearchMenu("popular");
  const { addFilter } = useFilter();

  return (
    <Flex column css={{ flex: "1", gap: "16px" }}>
      <Flex css={{ gap: "16px" }}>
        <Button
          variant="primary-outlined"
          css={{
            height: "36px",
            width: "36px",
            padding: 0,
            backgroundColor: "var(--primarylight2)",
          }}
          onClick={() => toggle()}
        >
          <MdOutlineCheck css={{ fontSize: "18px" }} />
        </Button>
        <Flex
          css={{
            gap: "12px",
            "*": {
              padding: "16px",
              height: "36px",
              alignItems: "center",
              flex: 1,
              wordBreak: "keep-all",
              whiteSpace: "nowrap",
            },
          }}
        >
          {TagGroup.map((menu) => (
            <Button
              key={menu.key}
              onClick={() => setCurrentMenu(menu.key)}
              css={{
                backgroundColor:
                  menu.key === currentMenu.key
                    ? "var(--primarylight)"
                    : "var(--positive)",
                border: 0,
              }}
            >
              {menu.displayName}
            </Button>
          ))}
        </Flex>
      </Flex>
      <Flex css={{ flexWrap: "wrap", gap: "12px" }}>
        {currentMenu.items.map((stack, i) => (
          <Button
            onClick={() => addFilter(stack)}
            key={i}
            css={{ border: 0, backgroundColor: "var(--positive1)" }}
          >
            {getDisplayTag(stack)}
          </Button>
        ))}
      </Flex>
    </Flex>
  );
};

const TagSearch = () => {
  const [isSearchMode, toggleIsSearchMode] = useToggle(false);

  return (
    <Flex css={{ flexWrap: "wrap", marginTop: "-56px" }}>
      <Box
        css={{
          flex: "99999 1 480px",
          overflow: "hidden",
        }}
      >
        {isSearchMode ? (
          <Search toggle={toggleIsSearchMode} />
        ) : (
          <Slider toggle={toggleIsSearchMode} />
        )}
      </Box>
      <div css={{ flex: "1 0 300px", height: 0, marginLeft: "24px" }}></div>
    </Flex>
  );
};

const FilterList = () => {
  const { filter, removeFilter } = useFilter();

  return (
    <Flex column css={{ gap: "12px", marginBottom: "31px" }}>
      <div
        css={{
          display: "flex",
          gap: "12px",
          "& > *": { fontSize: "14px" },
        }}
      >
        <Label css={{ padding: "0px 8px" }}>
          <MdOutlineLabel css={{ fontSize: "18px" }} />
          {!filter.length && (
            <span css={{ marginLeft: "4px" }}>
              태그를 3개까지 추가해 검색해보세요...
            </span>
          )}
        </Label>
        {filter.map((stack, i) => (
          <Label css={{ border: 0, fontSize: "16px" }} key={stack}>
            {getDisplayTag(stack)}
            <Button
              css={{
                width: "16px",
                height: "16px",
                borderRadius: "18px",
                padding: 0,
                margin: "0px -4px 0px 6px",
                fontSize: "11px",
                color: "var(--negative2)",
              }}
              onClick={() => removeFilter(i)}
            >
              <MdOutlineClose />
            </Button>
          </Label>
        ))}
      </div>
    </Flex>
  );
};

const FeaturedHeader = ({ content }: { content: string }) => (
  <Flex
    css={{
      alignItems: "center",
      fontSize: "14px",
      fontWeight: "600",
      lineHeight: "initial",
      marginBottom: "24px",
    }}
  >
    <span
      css={{
        display: "inline-block",
        width: "6px",
        height: "6px",
        borderRadius: "6px",
        backgroundColor: "var(--primary)",
        marginRight: "8px",
      }}
    />
    {content}
  </Flex>
);

const FeaturedItem = ({ userName, title, id }: IFeaturedItem) => {
  return (
    <Link passHref href={`/projects/${id}`}>
      <Flex as="a" column css={{ gap: "8px", cursor: "pointer" }}>
        <Flex
          css={{
            alignItems: "center",
            fontSize: "13px",
            gap: "8px",
          }}
        >
          <ProfilePlaceholder size={20} value={userName} />
          <span css={{ lineHeight: "initial" }}>{userName}</span>
        </Flex>
        <p
          css={{
            fontSize: "14px",
            fontWeight: "600",
            ...ellipsis(undefined, 2),
          }}
        >
          {title}
        </p>
      </Flex>
    </Link>
  );
};

const Featured = () => {
  const { data, isLoading, isError } = useQuery(
    "projects/featured",
    async () => {
      return (
        await http.get<{
          RECOMMEND: IFeaturedItem[];
          HOT: IFeaturedItem[];
        }>("/projects/featured")
      ).data;
    }
  );

  if (!data || isLoading || isError)
    return (
      <Flex
        css={{
          flex: "1 0 300px",
        }}
      />
    );

  return (
    <Flex
      css={{
        marginTop: "56px",
        flex: "1 0 300px",
        gap: "12px",
        alignSelf: "flex-start",
        position: "sticky",
        top: "71px",
      }}
      column
    >
      <Box css={{ padding: "14px 12px" }} column>
        <FeaturedHeader content="이런 프로젝트는 어떠신가요?" />
        <Flex column css={{ gap: "20px" }}>
          {data.RECOMMEND.map(({ title, userName, id }) => (
            <FeaturedItem key={id} title={title} userName={userName} id={id} />
          ))}
        </Flex>
      </Box>
      <Box css={{ padding: "14px 12px" }} column>
        <FeaturedHeader content="지금 HOT한 프로젝트 🔥" />
        <Flex column css={{ gap: "20px" }}>
          {data.HOT.map(({ title, userName, id }) => (
            <FeaturedItem key={id} title={title} userName={userName} id={id} />
          ))}
        </Flex>
      </Box>
    </Flex>
  );
};

const Project: NextPage = () => {
  const router = useRouter();
  const currentPage = parseInt(router.query.page as string) || 1;
  const { data, isLoading, isError } = useQuery("projects", () =>
    fetchProjects(
      undefined,
      currentPage,
      router.query.stacks
        ? ((Array.isArray(router.query.stacks)
            ? router.query.stacks
            : router.query.stacks.split(",")) as ITag[])
        : undefined
    )
  );

  const ProjectPagination = ({
    className,
  }: {
    className?: string | undefined;
  }) => (
    <Pagination
      css={{ justifyContent: "center", margin: "12px 0px" }}
      className={className}
      onClick={(i) => {
        router.push({
          query: {
            page: String(i),
          },
        });
      }}
      current={currentPage}
      end={data?.totalPages || 0}
    />
  );

  if (!data || isLoading || isError) return null;

  return (
    <ChildrenContainer>
      <SectionHeader>
        <SectionHeader.Title>프로젝트/스터디 찾기</SectionHeader.Title>
        <SectionHeader.Description>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        </SectionHeader.Description>
      </SectionHeader>
      <SectionBodyAlt>
        <FilterList />
      </SectionBodyAlt>

      <SectionBody>
        <TagSearch />
        <Flex css={{ gap: "24px", flexWrap: "wrap", marginBottom: "56px" }}>
          <Flex column css={{ flex: "99999 1 480px", marginBottom: "-56px" }}>
            <ProjectPagination />
            <Flex
              column
              css={{
                gap: "12px",
              }}
            >
              {data?.content.map((post, i) => (
                <Post post={post} key={i} />
              ))}
            </Flex>
            <ProjectPagination />
          </Flex>
          <Featured />
        </Flex>
      </SectionBody>
    </ChildrenContainer>
  );
};

const fetchProjects = async (_http = http, page = 1, stacks?: ITag[]) => {
  console.log(`link: /projects?page=${page}`);
  const res = await _http.get<IPageable<IPost[]>>(`/projects`, {
    params: {
      page,
      stacks: stacks?.join(","),
    },
  });
  return res.data;
};

export const getServerSideProps: GetServerSideProps = injectSession(
  async ({ http, context }) => {
    const queryClient = new QueryClient();

    await queryClient.prefetchQuery("projects", () =>
      fetchProjects(
        http,
        parseInt(context.query.page as string) || 1,
        context.query.stacks
          ? ((Array.isArray(context.query.stacks)
              ? context.query.stacks
              : context.query.stacks.split(",")) as ITag[])
          : undefined
      )
    );

    return {
      props: {
        dehydratedState: dehydrate(queryClient),
      },
    };
  }
);
export default Project;
