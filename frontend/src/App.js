import React from 'react';
import { SnackbarProvider } from 'notistack';
import GlobalStyles from './GlobalStyles';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { PATH } from './constants';
import {
  HomePage,
  ReviewPage,
  ReviewDetailPage,
  LoginPage,
  SignupPage,
  ReviewEditPage,
} from './pages';
import BaseLayout from './components/BaseLayout/BaseLayout';

const App = () => {
  return (
    <>
      <SnackbarProvider
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
        maxSnack={3}
      >
        <GlobalStyles />
        <BrowserRouter>
          <BaseLayout>
            <Switch>
              <Route exact path={PATH.HOME} component={HomePage} />
              <Route exact path={PATH.REVIEW} component={ReviewPage} />
              <Route exact path={`${PATH.REVIEW}/:id`} component={ReviewDetailPage} />
              <Route exact path={`${PATH.REVIEW}/:id/edit`} component={ReviewEditPage} />
              <Route exact path={PATH.LOGIN} component={LoginPage} />
              <Route exact path={PATH.SIGNUP} component={SignupPage} />
              <Redirect to={PATH.HOME} />
            </Switch>
          </BaseLayout>
        </BrowserRouter>
      </SnackbarProvider>
    </>
  );
};

export default App;
