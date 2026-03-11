# Clears all git history and creates a single fresh commit.
# Run from repo root. Keeps your remote URL; you will force-push after.

$ErrorActionPreference = "Stop"
$remoteUrl = git remote get-url origin 2>$null
Remove-Item -Recurse -Force .git
git init
git add -A
git commit -m "Initial commit: recruitment backend"
if ($remoteUrl) {
    git remote add origin $remoteUrl
    Write-Host "Run: git push --force origin main"
    Write-Host "If your branch is 'master' use: git branch -M main && git push --force origin main"
} else {
    Write-Host "No remote was set. Add with: git remote add origin <url>"
}
