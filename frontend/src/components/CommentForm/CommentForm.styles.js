import styled from '@emotion/styled';
import { FONT_COLOR, THEME_COLOR } from '../../constants';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  border: 0.1rem solid ${THEME_COLOR.PLACEHOLDER};
  border-radius: 0.8rem;
`;

const User = styled.div`
  display: flex;
  align-items: center;
  margin-top: 2rem;
  padding: 0 2rem;
  gap: 0.8rem;
  font-size: 1.4rem;
`;

const TextArea = styled.textarea`
  height: 8.4rem;
  margin: 2rem;
  border: 0.1rem solid transparent;
  border-radius: 0.4rem;
  resize: none;

  &:focus {
    border: 0.1rem solid ${THEME_COLOR.PRIMARY};
  }

  &::placeholder {
    color: ${THEME_COLOR.PLACEHOLDER};
  }
`;

const BottomContainer = styled.div`
  border-top: 0.1rem solid ${THEME_COLOR.PLACEHOLDER};
  display: flex;
  padding: 1.2rem 2rem;
  justify-content: space-between;
  align-items: center;
  font-size: 1.4rem;
`;

const CommentMaxCount = styled.span`
  color: ${FONT_COLOR.LIGHT_GRAY};
`;

export { Container, User, TextArea, BottomContainer, CommentMaxCount };
