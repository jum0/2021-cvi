import { useHistory } from 'react-router-dom';
import { ReviewItem } from '../../components';
import { Frame, LottieAnimation } from '../../components/common';
import { PAGING_SIZE, PATH, RESPONSE_STATE, THEME_COLOR } from '../../constants';
import {
  Container,
  LoadingContainer,
  ScrollLoadingContainer,
  Title,
  MyLikeReviewList,
  frameStyle,
} from './MyPageLikeReview.styles';
import { useSelector } from 'react-redux';
import { useCallback, useEffect, useState } from 'react';
import { useInView } from 'react-intersection-observer';
import { useLoading } from '../../hooks';
import { getMyLikeReviewListAsync } from '../../service';
import { NotFoundAnimation } from '../../assets/lotties';

const MyPageLikeReview = () => {
  const history = useHistory();
  const accessToken = useSelector((state) => state.authReducer.accessToken);
  const path = window.location.pathname;

  const [myLikeReviewList, setMyLikeReviewList] = useState([]);
  const [offset, setOffset] = useState(0);

  const [ref, inView] = useInView();
  const { showLoading, hideLoading, isLoading, Loading } = useLoading();
  const {
    showLoading: showScrollLoading,
    hideLoading: hideScrollLoading,
    isLoading: isScrollLoading,
    Loading: ScrollLoading,
  } = useLoading();

  const isLastPost = (index) => index === offset + PAGING_SIZE - 1;

  const getMyLikeReviewList = useCallback(async () => {
    if (!accessToken) return;

    const response = await getMyLikeReviewListAsync(accessToken, offset);

    if (response.state === RESPONSE_STATE.FAILURE) {
      alert('failure - getMyLikeReviewListAsync');

      return;
    }

    hideLoading();
    hideScrollLoading();
    setMyLikeReviewList((prevState) => [...prevState, ...response.data]);
  }, [offset, accessToken]);

  const goReviewDetailPage = (id) => {
    history.push(`${PATH.REVIEW}/${id}`);
  };

  useEffect(() => {
    // 게시글 아무것도 없을 때 처리
    if (offset === 0) showLoading();

    getMyLikeReviewList();
  }, [getMyLikeReviewList]);

  useEffect(() => {
    if (!inView) return;

    setOffset((preState) => preState + PAGING_SIZE);
    showScrollLoading();
  }, [inView]);

  return (
    <Container>
      <Title>좋아요 누른 글</Title>
      {isLoading ? (
        <LoadingContainer>
          <Loading isLoading={isLoading} backgroundColor={THEME_COLOR.WHITE} />
        </LoadingContainer>
      ) : myLikeReviewList.length === 0 ? (
        <LottieAnimation
          data={NotFoundAnimation}
          width="30rem"
          designer="Radhikakpor"
          description="좋아요 누른 글이 없습니다"
        />
      ) : (
        <Frame styles={frameStyle}>
          <MyLikeReviewList>
            {myLikeReviewList?.map((myLikeReview, index) => (
              <ReviewItem
                key={myLikeReview.id}
                review={myLikeReview}
                accessToken={accessToken}
                innerRef={isLastPost(index) ? ref : null}
                path={path}
                onClick={() => goReviewDetailPage(myLikeReview.id)}
              />
            ))}
          </MyLikeReviewList>
        </Frame>
      )}
      {isScrollLoading && (
        <ScrollLoadingContainer>
          <ScrollLoading isLoading={isScrollLoading} width="4rem" height="4rem" />
        </ScrollLoadingContainer>
      )}
    </Container>
  );
};

export default MyPageLikeReview;
