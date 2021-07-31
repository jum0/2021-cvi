import PropTypes from 'prop-types';
import { BUTTON_BACKGROUND_TYPE, BUTTON_SIZE_TYPE, Container } from './Button.styles';
import { THEME_COLOR } from '../../../constants';

const Button = ({
  children,
  backgroundType,
  sizeType,
  withIcon,
  color,
  isSelected,
  selectedStyles,
  styles,
  type,
  disabled,
  onClick,
}) => (
  <Container
    backgroundType={backgroundType}
    sizeType={sizeType}
    withIcon={withIcon}
    color={color}
    isSelected={isSelected}
    selectedStyles={selectedStyles}
    styles={styles}
    type={type}
    disabled={disabled}
    onClick={onClick}
  >
    {children}
  </Container>
);

Button.propTypes = {
  children: PropTypes.node.isRequired,
  backgroundType: PropTypes.string,
  sizeType: PropTypes.string,
  withIcon: PropTypes.bool,
  color: PropTypes.string,
  isSelected: PropTypes.bool,
  selectedStyles: PropTypes.object,
  styles: PropTypes.object,
  type: PropTypes.string,
  disabled: PropTypes.bool,
  onClick: PropTypes.func,
};

Button.defaultProps = {
  backgroundType: BUTTON_BACKGROUND_TYPE.FILLED,
  sizeType: BUTTON_SIZE_TYPE.MEDIUM,
  withIcon: false,
  color: THEME_COLOR.PRIMARY,
  isSelected: false,
  selectedStyles: null,
  styles: null,
  type: 'submit',
  disabled: false,
  onClick: () => {},
};

export default Button;
