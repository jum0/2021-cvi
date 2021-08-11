import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { FONT_COLOR, PALETTE } from '../../constants';

const Container = styled.div`
  margin-top: 4rem;
  padding: 2.5rem 8rem 6rem 8rem;

  @media screen and (max-width: 1024px) {
    padding: 0;
    margin-top: 0;
  }
`;

const FrameContent = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 55rem;
`;

const ButtonContainer = styled.div`
  padding: 2rem 2rem 0 1rem;

  @media screen and (max-width: 1024px) {
    padding: 2rem 0;
  }
`;

const Info = styled.div`
  padding: 2rem 3rem 2rem 3rem;
  display: flex;
  flex-direction: column;
  border-bottom: 0.15rem solid ${PALETTE.NAVY100};

  @media screen and (max-width: 1024px) {
    padding: 2rem;
  }
`;

const VaccinationInfo = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 3rem;
`;

const WriterInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 0.6rem;
`;

const ReviewInfo = styled.div`
  display: flex;
  align-items: center;
  flex-direction: row;
  margin-top: 1rem;
`;

const Writer = styled.div`
  font-size: 2rem;
  font-weight: 600;
`;

const InfoBottom = styled.div`
  display: flex;
  justify-content: space-between;
`;

const CreatedAt = styled.div`
  font-size: 1.2rem;
  color: ${FONT_COLOR.LIGHT_GRAY};
  padding-bottom: 0.2rem;
  margin: 0 1.6rem 0 0.3rem;
`;
const TextArea = styled.textarea`
  margin: 3rem;
  height: 100%;
  padding: 2rem 3rem;
  font-size: 1.6rem;
  color: ${FONT_COLOR.BLACK};

  @media screen and (max-width: 1024px) {
    margin: 2rem;
    padding: 2rem;
  }
`;

const ViewCount = styled.div`
  font-size: 1.2rem;
  color: ${FONT_COLOR.LIGHT_GRAY};
  padding-bottom: 0.2rem;
  margin-left: 0.3rem;
`;

const editButtonStyles = css`
  width: 100%;
  margin-top: 2.6rem;
  height: 4.8rem;
`;

const EditButtonContainer = styled.div`
  @media screen and (max-width: 1024px) {
    padding: 0 2rem;
  }
`;

export {
  Container,
  FrameContent,
  ButtonContainer,
  Info,
  VaccinationInfo,
  WriterInfo,
  ReviewInfo,
  Writer,
  InfoBottom,
  CreatedAt,
  TextArea,
  ViewCount,
  editButtonStyles,
  EditButtonContainer,
};
