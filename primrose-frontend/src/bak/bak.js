class Wizard extends React.Component {
  static propTypes = {
    onSubmit: PropTypes.func.isRequired
  }

  constructor(props) {
    super(props)
    this.state = {
      page: 0,
      values: props.initialValues || {}
    }
  }
  next = values =>
    this.setState(state => ({
      page: Math.min(state.page + 1, this.props.pages.length - 1),
      values
    }))

  previous = () =>
    this.setState(state => ({
      page: Math.max(state.page - 1, 0)
    }))

  validate = values => {
    const activePage = this.props.pages[this.state.page];
    return activePage.validate ? activePage.validate(values) : {}
  }

  handleSubmit = values => {
    const { children, onSubmit } = this.props
    const { page } = this.state
    const isLastPage = page === this.props.pages.length - 1
    if (isLastPage) {
      return onSubmit(values)
    } else {
      this.next(values)
    }
  }

  render() {
    const { pages } = this.props;
    const { page, values } = this.state;
    const activePage = pages[page];
    const isLastPage = page === pages.length - 1;
    const isFirstPage = page === 0;

    return (
      <Form
        initialValues={values}
        validate={this.validate}
        onSubmit={this.handleSubmit}>
        {({ handleSubmit, submitting, values }) => (
          <form onSubmit={handleSubmit}>
            <div className={classes.content}>{activePage}</div>
            <Grid className={classes.buttons} item container spacing={16}>
              <Grid item>
                <Button name="back" variant="raised" onClick={this.previous} disabled={isFirstPage}>Back</Button>
              </Grid>
              <Grid item>
                {isLastPage ?
                  <Button name="save" variant="raised" color="primary" type="submit" disabled={submitting}>Save</Button> :
                  <Button name="next" variant="raised" type="submit">Next</Button>}
              </Grid>
              {isOptional &&
                <Grid item>
                  <Button variant="raised" onClick={skip}>Skip</Button>
                </Grid>
              }
              <Grid className={classes.resetButton} item>
                <Button variant="raised" type="reset">Reset</Button>
              </Grid>
            </Grid>
          </form>
        )}
      </Form>
    )
  }
}