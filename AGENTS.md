# Repository Guidelines

## Project Structure & Module Organization
Agents are grouped by domain under `src/agents/<domain>/`, with each module exposing a single `<Domain>Agent` entry point. Shared orchestrators and utilities live in `src/services/`, and configuration defaults sit beside their modules in `src/config/` using `Path(__file__).with_name(...)` to load templates. Tests mirror the source tree inside `tests/`, with reusable fixtures in `tests/fixtures/` and narrative scenarios in `tests/scenarios/`. Store prompts or sample datasets in `assets/` and document larger artifacts in `assets/README.md` to keep provenance clear.

## Build, Test, and Development Commands
Create a fresh environment with `python -m venv .venv && source .venv/bin/activate`, then install dependencies via `pip install -e .[dev]`. Run `pytest` for the full suite or narrow the scope with `pytest -k pricing`. Lint with `ruff check src tests` and auto-format before committing using `ruff format`. Type-check with `mypy src` to guard interface contracts, and re-run the tooling after any dependency or typing change.

## Coding Style & Naming Conventions
Target Python 3.11+, four-space indentation, and 88-character lines. Public functions must carry type hints, and prefer `@dataclass` for shared state objects. Keep files snake_case, suffix async callables with `_async`, and ensure cross-agent communication flows through `src/services/` rather than direct imports between agent domains.

## Testing Guidelines
Author tests alongside their modules (e.g., `tests/agents/pricing/test_quote_agent.py`) and name fixtures `<name>_fixture`. Maintain ≥85% coverage using `pytest --cov=src --cov-report=term-missing`. Parameterize decision paths, snapshot prompt templates in `tests/fixtures/`, and keep end-to-end narratives up to date when agent workflows evolve.

## Commit & Pull Request Guidelines
Use Conventional Commits such as `feat: introduce pricing agent` with subjects under 72 characters, and link issues (`Closes #42`) when applicable. PRs should summarize intent, list validation commands run, and note any assets or datasets touched. Include logs or transcripts for behavioral changes and separate refactors from feature work to ease review.

## Security & Configuration Tips
Track sample settings in `.env.example`, load secrets from `.env.local`, and avoid committing credentials. Document new environment variables during review and update onboarding guides when external services or scopes change. Rotate API tokens promptly if a leak is suspected.
