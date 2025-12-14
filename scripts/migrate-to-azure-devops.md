# Migration Guide: GitHub to Azure DevOps

This guide will help you migrate your `eventful-android-app` repository from GitHub to Azure DevOps while maintaining history and enabling AI code review.

---

## 🎯 Migration Goals

1. ✅ Preserve full Git history
2. ✅ Set up Azure DevOps repository
3. ✅ Configure CI/CD pipeline
4. ✅ Enable AI code review
5. ✅ Keep GitHub as backup (optional)

---

## 📋 Prerequisites

- [x] Azure DevOps account (created in previous steps)
- [x] Azure DevOps project created
- [x] Access to GitHub repository: `https://github.com/nicolasmartino-dev/eventful-android-app`
- [x] Git installed locally
- [x] Azure CLI installed (optional, for advanced features)

---

## 🔄 Step 1: Prepare Local Repository

### 1.1 Clone GitHub Repository

```bash
cd /Users/nmartino/Documents/projects
git clone https://github.com/nicolasmartino-dev/eventful-android-app.git eventful-android-app-azure
cd eventful-android-app-azure
```

### 1.2 Verify Git History

```bash
# Check commit history
git log --oneline

# Verify all branches
git branch -a

# Check remote
git remote -v
```

You should see:
```
origin  https://github.com/nicolasmartino-dev/eventful-android-app.git (fetch)
origin  https://github.com/nicolasmartino-dev/eventful-android-app.git (push)
```

---

## 🚀 Step 2: Get Azure DevOps Repository URL

### 2.1 Get Repository URL

1. Go to Azure DevOps: `https://dev.azure.com/<your-org>/eventful-android-app`
2. Navigate to **Repos** → **Files**
3. Click **"Clone"** button
4. Copy the HTTPS URL (e.g., `https://dev.azure.com/eventful-dev/eventful-android-app/_git/eventful-android-app`)

### 2.2 Add Azure DevOps Remote

```bash
# Add Azure DevOps as a new remote
git remote add azure https://dev.azure.com/<your-org>/eventful-android-app/_git/eventful-android-app

# Verify remotes
git remote -v
```

You should now see:
```
azure   https://dev.azure.com/.../eventful-android-app/_git/eventful-android-app (fetch)
azure   https://dev.azure.com/.../eventful-android-app/_git/eventful-android-app (push)
origin  https://github.com/nicolasmartino-dev/eventful-android-app.git (fetch)
origin  https://github.com/nicolasmartino-dev/eventful-android-app.git (push)
```

---

## 📤 Step 3: Push to Azure DevOps

### 3.1 Push Main Branch

```bash
# Ensure you're on main branch
git checkout main

# Push to Azure DevOps (with all history)
git push azure main --all
```

### 3.2 Push All Branches (if you have multiple)

```bash
# Push all branches
git push azure --all

# Push all tags (if any)
git push azure --tags
```

### 3.3 Verify Migration

1. Go to Azure DevOps → **Repos** → **Files**
2. Verify all files are present
3. Go to **Commits** and verify history is intact
4. Check **Branches** to see all branches

---

## 🔧 Step 4: Add Pipeline File

### 4.1 Add azure-pipelines.yml

The pipeline file should already be in your repository. If not:

```bash
# Ensure pipeline file exists
ls -la azure-pipelines.yml

# If missing, copy from scripts directory
cp scripts/azure-pipelines.yml azure-pipelines.yml

# Commit and push
git add azure-pipelines.yml
git commit -m "Add Azure DevOps pipeline configuration"
git push azure main
```

### 4.2 Create Pipeline in Azure DevOps

1. Go to **Pipelines** → **Pipelines**
2. Click **"Create Pipeline"**
3. Select **"Azure Repos Git"**
4. Select repository: `eventful-android-app`
5. Choose **"Existing Azure Pipelines YAML file"**
6. Select branch: `main`
7. Select path: `/azure-pipelines.yml`
8. Click **"Continue"** → **"Run"**

---

## 🤖 Step 5: Enable AI Code Review

### 5.1 Enable Copilot for Azure DevOps

1. Go to **Project Settings** → **General**
2. Look for **"Azure DevOps Copilot"** or **"AI Features"**
3. Enable if available (may require organization-level settings)

### 5.2 Alternative: Use GitHub Copilot Integration

If Azure DevOps Copilot isn't available:

1. Go to **Project Settings** → **Service connections**
2. Create **GitHub** service connection
3. Use GitHub for PR reviews (see Step 6)

---

## 🔄 Step 6: Set Up Dual Remote (Optional)

If you want to keep both GitHub and Azure DevOps:

### 6.1 Configure Default Remote

```bash
# Set Azure DevOps as default for pushes
git remote set-url --push origin https://dev.azure.com/.../eventful-android-app/_git/eventful-android-app

# Or keep GitHub as default, Azure as secondary
# (current setup is fine)
```

### 6.2 Push to Both Remotes

Create a script to push to both:

```bash
# Create push script
cat > push-all.sh << 'EOF'
#!/bin/bash
git push origin main
git push azure main
EOF

chmod +x push-all.sh
```

---

## 🧪 Step 7: Test Migration

### 7.1 Create Test Branch

```bash
# Create test branch
git checkout -b test/azure-migration

# Make a small change
echo "# Test Azure DevOps Migration" >> TEST.md

# Commit
git add TEST.md
git commit -m "Test: Verify Azure DevOps migration"

# Push to Azure DevOps
git push azure test/azure-migration
```

### 7.2 Create Pull Request

1. In Azure DevOps, go to **Repos** → **Pull requests**
2. Click **"New pull request"**
3. Select:
   - **Source**: `test/azure-migration`
   - **Target**: `main`
4. Fill in title: "Test: Azure DevOps Migration"
5. Click **"Create"**

### 7.3 Test AI Code Review

1. In the PR, look for **"Review"** or **"AI Review"** button
2. Click to generate AI review
3. Review suggestions:
   - Code quality
   - Best practices
   - Security issues
   - Performance

### 7.4 Test Pipeline

1. The PR should trigger the pipeline
2. Check **Pipelines** → **Pipelines** for build status
3. Verify:
   - ✅ Build succeeds
   - ✅ APK is created
   - ✅ Tophat link is generated
   - ✅ PR comment is posted (if configured)

---

## 📊 Step 8: Verify Everything Works

### Checklist

- [ ] All files migrated to Azure DevOps
- [ ] Git history preserved (check commits)
- [ ] All branches migrated
- [ ] Pipeline runs successfully
- [ ] APK builds correctly
- [ ] Tophat link generated
- [ ] PR creation works
- [ ] AI code review works
- [ ] Build artifacts published

---

## 🔐 Step 9: Update Secrets & Configuration

### 9.1 Update Variable Group

1. Go to **Pipelines** → **Library**
2. Edit `Eventful-Android-Secrets`
3. Verify all variables are set:
   - `GOOGLE_MAPS_API_KEY`
   - `STORAGE_ACCOUNT_NAME`
   - `STORAGE_ACCOUNT_KEY`

### 9.2 Update Service Connections

1. Go to **Project Settings** → **Service connections**
2. Verify:
   - `Azure-Service-Connection` (for storage)
   - `GitHub-Connection` (for PR comments, optional)

---

## 🎯 Step 10: Update Documentation

### 10.1 Update README

Update your README to reflect Azure DevOps:

```markdown
## CI/CD

This project uses Azure DevOps for CI/CD.

### Build Status
[![Build Status](https://dev.azure.com/<org>/<project>/_apis/build/status/<pipeline-id>)](https://dev.azure.com/<org>/<project>/_build)

### Install with Tophat
After each build, a Tophat installation link is generated. Check the pipeline logs or PR comments for the link.
```

### 10.2 Update Contributing Guide

Add Azure DevOps workflow to CONTRIBUTING.md:

```markdown
## Development Workflow

1. Create a branch from `main`
2. Make your changes
3. Push to Azure DevOps
4. Create a Pull Request
5. AI code review will run automatically
6. After approval, merge to `main`
```

---

## 🔄 Step 11: Migration Complete!

### What's Now Available

✅ **Azure DevOps Repository**: Full Git history, all branches
✅ **CI/CD Pipeline**: Automated builds on every push/PR
✅ **Tophat Integration**: Automatic installation links
✅ **AI Code Review**: Automated code analysis on PRs
✅ **Build Artifacts**: APKs stored in Azure Storage
✅ **PR Comments**: Tophat links posted automatically

### Next Steps

1. **Team Onboarding**: Share Azure DevOps access with team
2. **Branch Policies**: Set up branch protection rules
3. **Notifications**: Configure build failure alerts
4. **Analytics**: Set up build analytics and reports

---

## 🐛 Troubleshooting

### Issue: "Permission denied" when pushing

**Solution**:
1. Check you're logged in to Azure DevOps
2. Verify repository permissions
3. Use Personal Access Token if needed:
   ```bash
   git remote set-url azure https://<token>@dev.azure.com/...
   ```

### Issue: "Large file" errors

**Solution**:
1. Check for large files in history:
   ```bash
   git rev-list --objects --all | git cat-file --batch-check='%(objecttype) %(objectname) %(objectsize) %(rest)' | awk '/^blob/ {print substr($0,6)}' | sort --numeric-sort --key=2 | tail -10
   ```
2. Use Git LFS for large files if needed

### Issue: Pipeline not triggering

**Solution**:
1. Check `azure-pipelines.yml` is in root
2. Verify YAML syntax is correct
3. Check branch triggers in pipeline YAML
4. Manually trigger: **Pipelines** → **Run pipeline**

---

## 📚 Additional Resources

- [Azure DevOps Git Tutorial](https://docs.microsoft.com/azure/devops/repos/git/)
- [Migrate from GitHub to Azure DevOps](https://docs.microsoft.com/azure/devops/repos/git/import-git-repository)
- [Azure Pipelines Documentation](https://docs.microsoft.com/azure/devops/pipelines/)

---

**Migration Complete!** 🎉

Your repository is now fully migrated to Azure DevOps with CI/CD and AI code review enabled!


