import {
  Flex,
  Box,
  Button,
  ButtonLink,
  SectionHeader,
  SectionBody,
  ProfilePlaceholder,
  Label,
  Hr,
} from "common/components";
import { ChildrenContainer, Viewer } from "components";
import { dehydrate, QueryClient, useQuery, useQueryClient } from "react-query";
import { http } from "common/services";
import { useRouter } from "next/router";
import {
  MdOutlineDelete,
  MdOutlineEdit,
  MdOutlineOpenInNew,
  MdOutlineStar,
  MdOutlineStarBorder,
} from "react-icons/md";
import useAuth from "hooks/useAuth";
import { IProjectContentData, IProjectData } from "types";
import { getDisplayTag, getDisplayColor } from "utils/tagDictionary";
import { GetServerSideProps, NextPage } from "next";
import { NotFound } from "components/NotFound";
import styled from "@emotion/styled";
import { injectSession } from "utils";

const ArticlePreview = ({
  data,
}: {
  data: {
    type: "string";
  } & IProjectContentData<string>;
}) => {
  return (
    <Box
      column
      css={{
        gap: "16px",
        width: "480px",
        padding: "16px",
      }}
    >
      <Flex css={{ alignItems: "center" }}>
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
        <b>{data.source}</b>
        에서 가져옴
      </Flex>
      <p>{data.content}...</p>
      <ButtonLink href={data.link} target="_blank" variant="primary-outlined">
        <MdOutlineOpenInNew css={{ fontSize: "16px" }} />
        원문 보기
      </ButtonLink>
    </Box>
  );
};

const ArticleHeader = ({ data }: { data: IProjectContentData<unknown> }) => {
  const auth = useAuth();
  const router = useRouter();
  const { id } = router.query as {
    id: string;
  };

  const ownership = auth.success && auth.user_id === data.userId;

  const handleDelete = async () => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    deleteProject(undefined, id)
      .then(() => router.push("/projects"))
      .catch(() => alert("알 수 없는 오류가 발생했습니다."));
  };

  const handleEdit = async () => {
    router.push(`/projects/edit/${id}`);
  };

  const queryClient = useQueryClient();

  const handleFav = async () => {
    const res = (
      await http.post("/projects/fav", {
        id: data.id,
        mode: !data.isfav,
      })
    ).data;
    if (res.success)
      queryClient.setQueryData<IProjectData | undefined>(
        ["project", id],
        (post) =>
          post && {
            ...post,
            isfav: data.isfav,
            fav: data.isfav ? post.fav + 1 : post.fav - 1,
          }
      );
  };

  const HeaderButton = styled(Button)`
    padding: 0;
    width: 36px;
  `;

  return (
    <Flex column>
      {data.stacks.length ? (
        <Flex css={{ gap: "8px", marginBottom: "12px" }}>
          {data.stacks.map((stack, i) => (
            <Label
              key={i}
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
        </Flex>
      ) : null}
      <p
        css={{
          fontSize: "28px",
          fontWeight: "500",
          lineHeight: "initial",
        }}
      >
        {data.postName}
      </p>
      <Flex
        css={{
          justifyContent: "space-between",
          alignItems: "center",
          gap: "12px",
          marginTop: "16px",
          fontSize: "14px",
        }}
      >
        <Flex
          css={{
            alignItems: "center",
            gap: "12px",
          }}
        >
          <ProfilePlaceholder value={data.userName} size={36} />
          <Flex column css={{ lineHeight: "initial" }}>
            <p css={{ fontWeight: "600" }}>{data.userName}</p>
            <p>{new Date(data.date).toLocaleString()}</p>
          </Flex>
        </Flex>

        <Flex
          css={{
            alignItems: "center",
            gap: "12px",
            fontSize: "18px",
            color: "var(--negative2)",
          }}
        >
          {ownership ? (
            <>
              <HeaderButton onClick={handleEdit}>
                <MdOutlineEdit />
              </HeaderButton>
              <HeaderButton onClick={handleDelete}>
                <MdOutlineDelete />
              </HeaderButton>
            </>
          ) : (
            <>
              <HeaderButton
                css={{ width: "initial", padding: "0px 12px", gap: "4px" }}
                onClick={handleFav}
              >
                {data.isfav ? <MdOutlineStar /> : <MdOutlineStarBorder />}
                <span css={{ fontSize: "14px" }}>{data.fav}</span>
              </HeaderButton>
            </>
          )}
        </Flex>
      </Flex>
    </Flex>
  );
};

const Article = () => {
  const router = useRouter();
  const { id } = router.query as {
    id: string;
  };

  const { data, isLoading, isError } = useQuery(["project", id], () =>
    fetchProject(undefined, id)
  );

  if (isLoading || isError) return null;
  if (!data) return <NotFound />;

  console.log(data);

  return (
    <Box
      responsive
      column
      css={{
        padding: "24px",
        marginBottom: "48px",
      }}
    >
      <ArticleHeader data={data} />
      <Hr css={{ marginTop: "16px" }} />
      <Flex
        css={{
          marginTop: "36px",
          marginBottom: "12px",
          lineHeight: 1.5,
          justifyContent: "center",
        }}
      >
        {data.type === "prosemirror" ? (
          <Viewer content={JSON.parse(data.content as unknown as string)} />
        ) : (
          <ArticlePreview data={data} />
        )}
      </Flex>
    </Box>
  );
};

const Page: NextPage = () => {
  return (
    <ChildrenContainer width={840}>
      <SectionHeader>
        <SectionHeader.Title>프로젝트/스터디 찾기</SectionHeader.Title>
        <SectionHeader.Description>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        </SectionHeader.Description>
      </SectionHeader>
      <SectionBody>
        <Article />
        <Box column>
          <div css={{ height: "240px" }}></div>
        </Box>
      </SectionBody>
    </ChildrenContainer>
  );
};

const fetchProject = async (_http = http, id: string) => {
  console.log(1);
  const res = await _http.get<IProjectData>(`/projects/${id}`);
  console.log(2);
  return res.data;
};

const deleteProject = async (_http = http, id: string) => {
  const res = await _http.post<IProjectData>(`/projects/delete`, { id });
  return res.data;
};

export default Page;
